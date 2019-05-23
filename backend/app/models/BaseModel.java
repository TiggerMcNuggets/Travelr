package models;

import io.ebean.Model;
import io.ebean.annotation.JsonIgnore;
import io.ebean.annotation.SoftDelete;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public class BaseModel extends Model {

  @Id
  public Long id;

  @JsonIgnore
  @SoftDelete
  public boolean deleted;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
