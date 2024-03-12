package com.example.plannerentity.dto.responce;

public record PostEmailRecordDTO(int userId,
                                 String emailTo,
                                 String subject,
                                 String text) {
}
