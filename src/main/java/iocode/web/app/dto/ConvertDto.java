package iocode.web.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConvertDto {
    private String fromCurrency;
    private String toCurrency;
    private double amount;
}
