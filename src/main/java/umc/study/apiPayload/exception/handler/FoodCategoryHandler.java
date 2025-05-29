package umc.study.apiPayload.exception.handler;

import umc.study.apiPayload.code.BaseErrorCode;

public class FoodCategoryHandler extends GeneralException {

    public FoodCategoryHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}