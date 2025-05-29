package umc.study.service.MemberService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import umc.study.apiPayload.code.status.ErrorStatus;
import umc.study.apiPayload.exception.handler.FoodCategoryHandler;
import umc.study.converter.MemberConverter;
import umc.study.converter.MemberPreferConverter;
import umc.study.domain.Member;
import umc.study.domain.FoodCategory;
import umc.study.domain.mapping.MemberPrefer;
import umc.study.repository.FoodCategoryRepository;
import umc.study.repository.MemberRepository;
import umc.study.web.dto.MemberRequestDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final FoodCategoryRepository foodCategoryRepository;
    private static final Logger log = LoggerFactory.getLogger(MemberCommandServiceImpl.class);

    @Override
    @Transactional
    public Member joinMember(MemberRequestDTO.JoinDto request) {

        log.info("request.getPreferCategory() = {}", request.getPreferCategory());

        Member newMember = MemberConverter.toMember(request);

        newMember.encodePassword(passwordEncoder.encode(request.getPassword()));

        List<FoodCategory> foodCategoryList = request.getPreferCategory().stream()
                .map(category -> {
                    log.info("🔍 Trying to find FoodCategory with id = {}", category);
                    Optional<FoodCategory> optional = foodCategoryRepository.findById(category);
                    if (optional.isPresent()) {
                        log.info("Found FoodCategory: {} - {}", category, optional.get().getName());
                        return optional.get();
                    } else {
                        log.warn("FoodCategory NOT FOUND: id = {}", category);
                        throw new FoodCategoryHandler(ErrorStatus.FOOD_CATEGORY_NOT_FOUND);
                    }
//                    return foodCategoryRepository.findById(Long.valueOf(category)).orElseThrow(() -> new FoodCategoryHandler(ErrorStatus.FOOD_CATEGORY_NOT_FOUND));
                }).collect(Collectors.toList());

        log.info("Total food categories found: {}", foodCategoryList.size());

        List<MemberPrefer> memberPreferList = MemberPreferConverter.toMemberPreferList(foodCategoryList);

        memberPreferList.forEach(memberPrefer -> {memberPrefer.setMember(newMember);});

        Member savedMember = memberRepository.save(newMember);
        log.info("Saved member: {}", savedMember.getId(), savedMember.getName());
        return savedMember;


    }
}