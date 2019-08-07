package models;

import io.ebean.Finder;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Entity
@Inheritance
public abstract class TripNode extends BaseModel {
    public String getName() throws Exception {
        throw new Exception();
    }

    @ManyToOne
    public TripComposite tripComposite;
}
