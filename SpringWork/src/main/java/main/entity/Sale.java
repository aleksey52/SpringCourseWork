package main.entity;

import javax.persistence.*;

@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "good_id")
    private Good good;

    @Column
    private Long good_count;

    @Column
    private String create_date;

    public Sale() {

    }

    public Sale(Good good, Long good_count, String create_date) {
        this.good = good;
        this.good_count = good_count;
        this.create_date = create_date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    public Long getGood_count() {
        return good_count;
    }

    public void setGood_count(Long good_count) {
        this.good_count = good_count;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
}
