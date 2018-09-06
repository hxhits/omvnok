package vn.com.omart.backend.application.response;

import lombok.Data;
import vn.com.omart.backend.domain.model.Item;
import vn.com.omart.backend.domain.model.ItemGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ItemDTO {


    private Long poiId;

    private Long id;
    private String name;
    private String description;
    private double unitPrice;
    private String type;

    private List<ImageDTO> avatarImage;
    private List<ImageDTO> coverImage;
    private List<ImageDTO> featuredImage;
    
    private boolean isOutOfStock;

    private String createdBy;
    private Date createdAt;

    private String updatedBy;
    private Date updatedAt;
    private List<PoiItemGroupDTO> itemGroups;
    private boolean isPosted;
    private PointOfInterestDTO poi;

    public static ItemDTO toFullDTO(Item model) {
    	ItemDTO dto = from(model);
    	if(model.getPoi()!=null) {
    		dto.setPoi(PointOfInterestDTO.toBasicDTO(model.getPoi()));
    	}
    	return dto;
    }
    
    public static ItemDTO from(Item model) {
        ItemDTO dto = new ItemDTO();

        dto.poiId = model.getPoi().id();

        dto.id = model.id();
        dto.name = model.name();
        dto.unitPrice = model.unitPrice();
//        dto. type = model.type();
        dto.description = model.description();
        dto.avatarImage = model.avatarImage().stream().map(ImageDTO::from).collect(Collectors.toList());
        dto.coverImage = model.coverImage().stream().map(ImageDTO::from).collect(Collectors.toList());
        dto.featuredImage = model.featuredImage().stream().map(ImageDTO::from).collect(Collectors.toList());
        
        dto.isOutOfStock = model.isOutOfStock();
        dto.isPosted = model.isPosted();
        dto.createdBy = model.createdBy();
        dto.createdAt = model.createdAt();

        dto.updatedBy = model.updatedBy();
        dto.updatedAt = model.updatedAt();
        if(model.getItemGroups()!=null) {
        	List<PoiItemGroupDTO> itemGroupsRs = new ArrayList<>();
        	for( ItemGroup itemGroup : model.getItemGroups()) {
        		if(itemGroup.getGroup()!=null) {
        			PoiItemGroupDTO poiItemGrp = PoiItemGroupDTO.toBasicDTO(itemGroup.getGroup());
        			itemGroupsRs.add(poiItemGrp);
        		}
        	}
        	dto.itemGroups = itemGroupsRs;
        }
        return dto;
    }

//    public static ItemDTO fromDetail(Item model) {
//        ItemDTO dto = from(model);
//        dto.description = model.description();
//        return dto;
//    }
}
