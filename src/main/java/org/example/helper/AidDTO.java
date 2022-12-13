package org.example.helper;

import jade.core.AID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AidDTO {

    private String name;
    private String hap;
    private String[] addressesArray;

    public AidDTO(AID myAgent) {
        this.name = myAgent.getName();
        this.hap = myAgent.getHap();
        this.addressesArray = myAgent.getAddressesArray();
    }

    public AID toAid() {
        AID aid = new AID(name, true);
        if (addressesArray != null) {
            for (String addresses : addressesArray) {
                aid.addAddresses(addresses);
            }
        }
        return aid;
    }
}
