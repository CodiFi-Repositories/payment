//package in.codifi.ambalal.model;
//
//import lombok.Getter;
//import lombok.Setter;
//
////@Data
//@Setter
//@Getter
//public class AdminControlRequest {
//    private String dataKey;
//    private boolean dataValue;
//}

package in.codifi.ambalal.model;

public class AdminControlRequest {
    private String dataKey;
    private boolean dataValue;

    // Getters & Setters
    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public boolean isDataValue() {
        return dataValue;
    }

    public void setDataValue(boolean dataValue) {
        this.dataValue = dataValue;
    }
}
