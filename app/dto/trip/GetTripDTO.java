package dto.trip;

import play.data.validation.Constraints;

import java.util.List;

public class GetTripDTO {

    public long id;

    @Constraints.Required
    public String name;

    // TODO: Find a way to allow empty list as @Contstraints.Required doesn't allow it
    public List<NodeDTO> nodes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NodeDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeDTO> nodes) {
        this.nodes = nodes;
    }

    public GetTripDTO() {

    }
}
