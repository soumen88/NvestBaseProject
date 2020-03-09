package nvest.com.nvestlibrary.databaseFiles.dao.formulatable;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;


@Entity(tableName = NvestLibraryConfig.FORMULA_TABLE)
public class FormulaRoom {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private Long id;

    @ColumnInfo(name = "FORMULAID")
    private int formulaId;

    @ColumnInfo(name = "PRODUCTID")
    private int productId;

    @ColumnInfo(name = "FORMULAKEYWORD")
    private String formulaKeyword;

    @ColumnInfo(name = "FORMULAWITHFUNCTION")
    private String formulawithfunction;

    @ColumnInfo(name = "FORMULABASIC")
    private String formulabasic;

    @ColumnInfo(name = "FORMULAEXTENDED")
    private String formulaExtended;

    @ColumnInfo(name = "ISOUTPUT")
    private boolean isOutput;

    @ColumnInfo(name = "ISINTERIMKW")
    private boolean isInterimKe;

    @ColumnInfo(name = "OUTPUTLOOP")
    private boolean outputLoop;

    @ColumnInfo(name = "SUMORYREND")
    private boolean sumOrYrEnd;

    @ColumnInfo(name = "DESCRIPTION")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(int formulaId) {
        this.formulaId = formulaId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getFormulaKeyword() {
        return formulaKeyword;
    }

    public void setFormulaKeyword(String formulaKeyword) {
        this.formulaKeyword = formulaKeyword;
    }

    public String getFormulawithfunction() {
        return formulawithfunction;
    }

    public void setFormulawithfunction(String formulawithfunction) {
        this.formulawithfunction = formulawithfunction;
    }

    public String getFormulabasic() {
        return formulabasic;
    }

    public void setFormulabasic(String formulabasic) {
        this.formulabasic = formulabasic;
    }

    public String getFormulaExtended() {
        return formulaExtended;
    }

    public void setFormulaExtended(String formulaExtended) {
        this.formulaExtended = formulaExtended;
    }

    public boolean isOutput() {
        return isOutput;
    }

    public void setOutput(boolean output) {
        isOutput = output;
    }

    public boolean isInterimKe() {
        return isInterimKe;
    }

    public void setInterimKe(boolean interimKe) {
        isInterimKe = interimKe;
    }

    public boolean isOutputLoop() {
        return outputLoop;
    }

    public void setOutputLoop(boolean outputLoop) {
        this.outputLoop = outputLoop;
    }

    public boolean isSumOrYrEnd() {
        return sumOrYrEnd;
    }

    public void setSumOrYrEnd(boolean sumOrYrEnd) {
        this.sumOrYrEnd = sumOrYrEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
