package telenor.rest.wrapper;

import telenor.service.dto.ProductSearchResponseDTO;

import java.io.Serializable;
import java.util.List;

public class DataWrapper implements Serializable {

    private List<ProductSearchResponseDTO> data;

    public DataWrapper() {
    }

    public DataWrapper(List<ProductSearchResponseDTO> data) {
        this.data = data;
    }

    public List<ProductSearchResponseDTO> getData() {
        return data;
    }

    public void setData(List<ProductSearchResponseDTO> data) {
        this.data = data;
    }
}
