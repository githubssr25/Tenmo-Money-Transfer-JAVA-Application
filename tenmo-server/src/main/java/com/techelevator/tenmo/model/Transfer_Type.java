package com.techelevator.tenmo.model;

import javax.validation.constraints.NotNull;

public class Transfer_Type {
    @NotNull
    private int transfer_Type_Id;
    private String transfer_Type_Desc;

   //Constructor
    public Transfer_Type(@NotNull int transfer_Type_Id, String transfer_Type_Desc) {
        this.transfer_Type_Id = transfer_Type_Id;
        this.transfer_Type_Desc = transfer_Type_Desc;
    }

    public int getTransfer_Type_Id() {
        return transfer_Type_Id;
    }

    public void setTransfer_Type_Id(int transfer_Type_Id) {
        this.transfer_Type_Id = transfer_Type_Id;
    }

    public String getTransfer_Type_Desc() {
        return transfer_Type_Desc;
    }

    public void setTransfer_Type_Desc(String transfer_Type_Desc) {
        this.transfer_Type_Desc = transfer_Type_Desc;
    }
}
