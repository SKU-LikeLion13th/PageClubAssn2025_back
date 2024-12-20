package likelion13.page.service;

import java.util.Base64;

public class ImageUtility {

    // 프론트에서 받은 이미지(이미지 그 자체) => getByte()함수로 byte배열로 변환
    // 데이터베이스에 저장된 이미지(byte 배열) => encoder로 다시 프론트에 뿌려주기(이미지 그 자체)
    public static String encodeImage(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
