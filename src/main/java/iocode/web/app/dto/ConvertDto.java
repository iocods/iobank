package iocode.web.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A data transfer object (DTO) for currency conversion requests.
 * This DTO holds the necessary information to perform a currency conversion.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConvertDto {

    /**
     * The ISO 4217 currency code of the original currency.
     * For example, "USD" for United States Dollar, "EUR" for Euro, etc.
     */
    private String fromCurrency;

    /**
     * The ISO 4217 currency code of the target currency.
     * For example, "USD" for United States Dollar, "EUR" for Euro, etc.
     */
    private String toCurrency;

    /**
     * The amount of money to be converted.
     * This value should be in the original currency (fromCurrency).
     */
    private double amount;
}
