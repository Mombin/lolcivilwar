package kr.co.mcedu.test.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "system", schema = "lol")
public class TestEntity {
    @Id
    @Column(name = "property_name")
    private String propertyName;
    @Column(name = "property_value1")
    private String propertyValue1;
}
