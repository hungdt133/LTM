package UDP;

import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 20161107;

    private String id;
    private String code;
    private String name;
    private int quantity;

    public Product(String id, String code, String name, int quantity) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.quantity = quantity;
    }

    // Getter + Setter
    public String getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }

    public void setName(String name) { this.name = name; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}