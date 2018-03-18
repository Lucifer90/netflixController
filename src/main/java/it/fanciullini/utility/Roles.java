package it.fanciullini.utility;

public enum Roles implements EnumConverter<Integer>
{
    ADMIN(0, "Admin"),
    STANDARD(1, "Standard");

    private int code;
    private String description;

    private Roles(int code, String description){
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
