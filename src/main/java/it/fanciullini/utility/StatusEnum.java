package it.fanciullini.utility;

public enum StatusEnum implements EnumConverter<Integer>
{
    TOBEPAYED(0, "Da Pagare"),
    PAYED(1, "Pagato"),
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
