package iocode.web.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for a bank transfer operation.
 * This class encapsulates the necessary information required for a transfer operation.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {

    /**
     * The account number of the recipient.
     */
    private long recipientAccountNumber;

    /**
     * The amount to be transferred.
     */
    private double amount;

    /**
     * The unique code for the transfer operation.
     */
    private String code;
}