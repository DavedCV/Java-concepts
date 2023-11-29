package sia.tacocloud.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import sia.tacocloud.domain.TacoOrder;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {
    List<TacoOrder> findByDeliveryZip(String deliveryZip);

    List<TacoOrder> readOrdersByDeliveryZipAndPlacedAtBetween(String deliveryZip, LocalDate startDate,
                                                              LocalDate endDate);

    List<TacoOrder> findByDeliveryToAndDeliveryCityAllIgnoresCase(String deliveryTo, String deliveryCity);

    List<TacoOrder> findByDeliveryCityOrderByDeliveryTo(String city);

    @Query("Order o where o.deliveryCity='Seattle'")
    List<TacoOrder> readOrdersDeliveredInSeattle();
}
