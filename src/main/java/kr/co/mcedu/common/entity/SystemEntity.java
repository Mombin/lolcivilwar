package kr.co.mcedu.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@DynamicUpdate
@Entity(name = "system" )
@Table(name = "system" , schema = "lol")
public class SystemEntity
        extends BaseTimeEntity {

    @Id
    @Column(name = "property_name" , length = 30)
    private String propertyName;
    @Column(name = "property_value1", length = 500)
    private String propertyValue1;
    @Column(name = "property_value2", length = 500)
    private String propertyValue2;
}


