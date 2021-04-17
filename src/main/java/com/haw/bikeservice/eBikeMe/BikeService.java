package com.haw.bikeservice.eBikeMe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BikeService {

    private final CustomerRepository customerRepository;

    private final BikeRepository bikeRepository;

    @Autowired
    public BikeService(CustomerRepository customerRepository, BikeRepository bikeRepository) {
        this.customerRepository = customerRepository;
        this.bikeRepository = bikeRepository;
    }

    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }

    public List<Bike> getBikes(){
        return bikeRepository.findAll();
    }

    /**
     * Methode zum Fahrräder entleihen.
     * Wenn das Fahrrad noch nicht im Repository vorhanden ist,
     * oder aber es ist defekt/ in Reparatur, wird eine Exception geworfen
     * Wenn der Kunde noch nicht im Repository ist, wird er eingetragen
     *
     * @param customer Name des Kunden
     * @param bikeName Name des Fahrrads
     * @throws BikeNotAvailableException Exception
     */
    @Transactional
    public void lendingBike(String customer, String bikeName) throws BikeNotAvailableException {
        Bike bike = bikeRepository.findByName(bikeName)
                .orElseThrow(() -> new BikeNotAvailableException(bikeName));

        if (bike.getStatus() == Status.DEFECT || bike.getStatus() == Status.UNDER_REPAIR){
            throw new BikeNotAvailableException(bikeName);
        }

        if(!customerRepository.findByLastName(customer).isPresent()){
            customerRepository.save(new Customer(customer));
        }
        Customer cus = customerRepository.findByLastName(customer).get();
        cus.addBike(bike);
    }

    /**
     * Methode zum melden eines defekten Fahrrads
     * @param bikeName Name des Fahrrads
     */
    public void reportDefect(String bikeName) throws BikeNotAvailableException {
        Bike bike = bikeRepository.findByName(bikeName)
                .orElseThrow(() -> new BikeNotAvailableException(bikeName));
        bike.setStatus(Status.DEFECT);
    }

    /**
     * Methode zur Zückgabe eines Fahrrads
     *
     * @param cusName Name des Kunden
     * @param bikeName Name des Fahrrads
     * @throws CustomerNotFoundException Exception
     */
    public void returnBike(String cusName, String bikeName) throws CustomerNotFoundException, BikeNotAvailableException {
        Customer customer = customerRepository.findByLastName(cusName)
                .orElseThrow(() -> new CustomerNotFoundException("Customer does not exist"));
        Bike bike = bikeRepository.findByName(bikeName)
                .orElseThrow(() -> new BikeNotAvailableException(bikeName));
        if (customer.getBikes().contains(bike)) {
            customer.getBikes().remove(bike);
        }else{
            throw new IllegalArgumentException("This Bike is not lent by Mr./Mrs. " + cusName);
        }
    }

    /**
     * Methoden zum Einfügen ein neues Fahrrad
     *
     * @param bikeName Name des Fahrrad
     * @param status Status des Fahrrad
     */
    public void addBike(String bikeName, Status status){
        bikeRepository.save(new Bike(bikeName, status));
    }

    /**
     * Methode zum Entfernen eines Fahrrads
     * @param bikeName Name des Fahrrad
     */
    public void removeBike(String bikeName){
        Bike bike = bikeRepository.findByName(bikeName)
                .orElseThrow(() ->  new IllegalArgumentException("This Bike does not exist"));
        bikeRepository.delete(bike);
    }

    /**
     * Methode zum Makieren eines repariertem Fahhrad
     * es wird auf dem Status Status.AVAILABLE gesetzt
     * @param bikeName Name des Fahrrads
     */
    public void markStatusRepaired(String bikeName){
        Bike bike = bikeRepository.findByName(bikeName)
                .orElseThrow(() ->  new IllegalArgumentException("This Bike does not exist"));

//        bike.setStatus(Status.REPAIRED);
        bike.setStatus(Status.AVAILABLE);
    }
}
