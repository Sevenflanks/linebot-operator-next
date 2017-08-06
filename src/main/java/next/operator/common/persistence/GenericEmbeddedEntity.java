package next.operator.common.persistence;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@SuppressWarnings("serial")
@MappedSuperclass
@Getter
@Setter
public class GenericEmbeddedEntity implements Serializable {

}
