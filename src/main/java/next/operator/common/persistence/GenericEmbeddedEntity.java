package next.operator.common.persistence;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@SuppressWarnings("serial")
@MappedSuperclass
@Getter
@Setter
public class GenericEmbeddedEntity implements Serializable {

}
