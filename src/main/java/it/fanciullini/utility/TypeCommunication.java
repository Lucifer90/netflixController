package it.fanciullini.utility;

public enum TypeCommunication implements EnumConverter<Integer>
{
    MAIL(0, "Mail"),
    CHAT(1, "Chat");

    private int code;
    private String description;

    private TypeCommunication(int code, String description){
        this.code = code;
        this.description = description;
    }

    @Override
    public Integer getDbValue(){
        return code;
    }
}
