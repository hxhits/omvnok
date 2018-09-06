package vn.com.omart.backend.port.adapter.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.domain.model.BookCar;
import vn.com.omart.backend.domain.model.BookCarRepository;
import vn.com.omart.backend.domain.model.Item;
import vn.com.omart.backend.domain.model.ItemGroup;
import vn.com.omart.backend.domain.model.ItemGroupRepository;
import vn.com.omart.backend.domain.model.ItemRepository;
import vn.com.omart.backend.domain.model.Order;
import vn.com.omart.backend.domain.model.OrderRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.backend.domain.model.Timeline;
import vn.com.omart.backend.domain.model.TimelineRepository;
import vn.com.omart.backend.domain.model.Video;

@RestController
@RequestMapping("/v1/migration")
public class DataMigrationResource {
	
	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private BookCarRepository bookCarRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ItemGroupRepository itemGroupRepository;
	
	@Autowired
	private TimelineRepository timelineRepository;
	
	@GetMapping(value="timeline/video")
	public void migrateVideoTimeline() {
//		List<Timeline> timelines = timelineRepository.findByVideosIsNull();
//		List<Timeline> entities = new ArrayList<>();
//		for(Timeline item : timelines) {
//			if(item.getVideos() == null) {
//				item.setVideos(new ArrayList<>());
//				entities.add(item);
//			}
//		}
//		if(!entities.isEmpty()) {
//			timelineRepository.save(entities);
//		}
		
		Timeline mt = timelineRepository.findOne(9L);
		if(mt!=null) {
			if(mt.getVideos()!=null) {
				if(mt.getVideos().isEmpty()) {
					List<Video> myVideos = new ArrayList<>();
					mt.setVideos(myVideos);
//					if(StringUtils.isBlank(mt.getDescription())) {
//						mt.setDescription("");
//					}else {
//						String des = mt.getDescription();
//						mt.setDescription(des);
//					}
					mt.setDescription("jj");
					timelineRepository.saveAndFlush(mt);//.save(mt);
				}
				
			}
		}
		
	}
	
	@GetMapping(value="/pois/discount")
	public void updateDiscount() {
		Iterable<PointOfInterest> pois = pointOfInterestRepository.findAll();
		pois.forEach(item->{
			item.setDiscount(5);
		});
		pointOfInterestRepository.save(pois);
	}
	
	@GetMapping(value="/bookcar-poiId")
	public void bookcarMigrationData() {
		
		//PageRequest pageable = new PageRequest(0, 2);
		List<BookCar> entitiesUp = new ArrayList<>();
		
		List<BookCar> entities = bookCarRepository.getDataForMigration();//(pageable);
		
		entities.forEach(entity->{
			if(entity.getOrderId()!=null) {
				Order order = orderRepository.findOne(entity.getOrderId());
				if(order!=null) {
					entity.setPoiId(order.getPoi().id());
				}
			}
		});
		if(!entities.isEmpty()) {
			bookCarRepository.save(entities);
		}
	}
	
	@GetMapping(value="/item-group")
	public void itemGroupMigration() {
		//PageRequest pageable = new PageRequest(0, 20);
		List<Item> items = itemRepository.findAll();
		if(items!=null) {
//			List<ItemGroup> entities = new ArrayList<>();
//			for(Item item: items) {
//				PointOfInterest poi = pointOfInterestRepository.findOne(item.getPoiId());
//				if(poi!=null) {
//					List<ItemGroup> listItems = itemGroupRepository.findByItemAndPoiId(item, poi.id());
//					if(listItems == null || listItems.isEmpty()) {
//						ItemGroup entity = new ItemGroup();
//						entity.setItem(item);
//						entity.setPoiId(poi.id());
//						entities.add(entity);
//					}
//				}
//			}
//			if(!entities.isEmpty()) {
//				itemGroupRepository.save(entities);
//			}
		}
	}
	
}
