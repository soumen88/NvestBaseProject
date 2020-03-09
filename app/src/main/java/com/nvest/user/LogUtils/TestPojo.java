package com.nvest.user.LogUtils;

import android.arch.persistence.room.ColumnInfo;

public class TestPojo {
    @ColumnInfo(name = "FormulaIdTestColumn")
    public final int formulaId;
    @ColumnInfo(name = "ProductIdTestColumn")
    public final int productId;
    @ColumnInfo(name = "PREMIUMRATE")
    public final double premiumrate;

    public TestPojo(int formulaId, int productId, double premiumrate) {
        this.formulaId = formulaId;
        this.productId = productId;
        this.premiumrate = premiumrate;
    }

    public int getFormulaId() {
        return formulaId;
    }

    public int getProductId() {
        return productId;
    }

    public double getPremiumrate() {
        return premiumrate;
    }
}
