package models;

import java.sql.Timestamp;

public class Sale {
    public enum PaymentMethod { CASH, CREDIT }

    private int id;
    private int usrId;
    private Timestamp createdAtTime;
    private float totalPrice;
    private PaymentMethod paymentMethod;

    // Constructors
    public Sale(int id, int usrId, Timestamp createdAtTime, float totalPrice, PaymentMethod paymentMethod) {
        this.id = id;
        this.usrId = usrId;
        this.createdAtTime = createdAtTime;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
    }

    public Sale(int usrId, float totalPrice, PaymentMethod paymentMethod) {
        this.usrId = usrId;
        this.createdAtTime = new Timestamp(System.currentTimeMillis());
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
    }

    // Getters and setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsrId() { return usrId; }
    public void setUsrId(int usrId) { this.usrId = usrId; }

    public Timestamp getCreatedAtTime() { return createdAtTime; }
    public void setCreatedAtTime(Timestamp createdAtTime) { this.createdAtTime = createdAtTime; }

    public float getTotalPrice() { return totalPrice; }
    public void setTotalPrice(float totalPrice) { this.totalPrice = totalPrice; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
}
