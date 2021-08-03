package main.entity;

import javax.persistence.*;

@Entity
@Table(name = "warehouse_one")
public class WarehouseOne {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "good_id")
    private Good good;

    @Column
    private Long good_count;

    public WarehouseOne() {
    }

    public WarehouseOne(Good good, Long good_count) {
        this.good = good;
        this.good_count = good_count;
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
}
