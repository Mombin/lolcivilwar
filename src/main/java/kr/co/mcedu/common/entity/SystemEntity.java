package kr.co.mcedu.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "system" )
@Table(name = "system" , schema = "lol")
public class SystemEntity {

    @Id
    @Column(name = "property_name" , length = 30)
    private String propertyName;
    @Column(name = "property_value1", length = 500)
    private String propertyValue1;
    @Column(name = "property_value2", length = 500)
    private String propertyValue2;
}


