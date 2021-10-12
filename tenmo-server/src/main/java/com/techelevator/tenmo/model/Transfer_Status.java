package com.techelevator.tenmo.model;

import javax.validation.constraints.NotNull;

public class Transfer_Status {

    @NotNull
    private int transfer_Status_Id;
    private String transfer_Status_Desc;

    public Transfer_Status(@NotNull int transfer_Status_Id, String transfer_Status_Desc) {
        this.transfer_Status_Id = transfer_Status_Id;
        this.transfer_Status_Desc = transfer_Status_Desc;
    }

    //Geeters and Setters
    public int getTransfer_Status_Id() {
        return transfer_Status_Id;
    }

    public void setTransfer_Status_Id(int transfer_Status_Id) {
        this.transfer_Status_Id = transfer_Status_Id;
    }

    public String getTransfer_Status_Desc() {
        return transfer_Status_Desc;
    }

    public void setTransfer_Status_Desc(String transfer_Status_Desc) {
        this.transfer_Status_Desc = transfer_Status_Desc;
    }
}
