package it.fanciullini.utility;

public enum StatusEnum implements EnumConverter<Integer>
{
    PAYED(0, "Pagato"),
    TOBEPAYED(1, "Da Pagare"),
    INPAYMENT(2, "In scadenza"),
    EXPIRED(3, "Scaduto");

    private int code;
    private String description;

    private StatusEnum(int code, String description){
        this.code = code;
        this.description = description;
    }

    @Override
    public Integer getDbValue(){
        return code;
    }

    public Boolean hasPermission(Roles role2){
        return this.getDbValue()<=role2.getDbValue();
    }
}
