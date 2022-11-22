package com.solverscrum.shopping.vo;

import com.solverscrum.shopping.entity.Products;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class SupplierVo {
    @NotNull(message = "Supplier Name is required") @Size(min = 4, message = "Name should be atleast 4 characters")
    private String supplierName;
    @NotNull(message = "Supplier address is required")
    private String address;
    @NotNull(message = "Supplier city is required")
    private String city;
    @NotNull(message = "Supplier postal code is required") @Min(value = 100000, message = "Postal code should be 6 digits") @Max(value = 999999,message = "Postal code should be 6 digits")
    private int postalCode;
    @NotNull(message = "Supplier phone is required") @Min(10000000)
    private int phone;
}
