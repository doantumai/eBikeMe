package com.haw.bikeservice.eBikeMe;

import com.haw.bikeservice.eBikeMe.Bike.Bike;
import com.haw.bikeservice.eBikeMe.Customer.Customer;
import com.haw.bikeservice.eBikeMe.Customer.Status;
import com.haw.bikeservice.eBikeMe.Exceptions.BikeNotAvailableException;
import com.haw.bikeservice.eBikeMe.Repositories.BikeRepository;
import com.haw.bikeservice.eBikeMe.Repositories.CustomerRepository;
import com.haw.bikeservice.eBikeMe.Exceptions.CustomerNotFoundException;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Klasse zur Verwaltung von Ausleihen für Fahrraeder. Die folgenden Funktionen sind definiert:
 * - funktionstüchtige Fahrräder zu entleihen
 * - Fahrräder zurückzugeben
 * - Defekte Fahrräder zu melden
 * - Den Ausleihstatus aller Fahrräder einzusehen
 * - Fahrräder hinzuzufügen bzw. aus dem Inventar zu entfernen
 * - Fahrräder wieder als „repariert“ zu markieren
 *
 * @author Tu Mai Doan (tumai.doan@haw-hamburg.de)
 */
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
     * @param cusLastName Name des Kunden
     * @param bikeName Name des Fahrrads
     * @throws BikeNotAvailableException Exception
     */
    @Transactional
    public void lendBike(String cusLastName, String bikeName) throws BikeNotAvailableException {
        Bike bike = bikeRepository.findByName(bikeName)
                .orElseThrow(() -> new BikeNotAvailableException(bikeName));

        if (bike.getStatus() == Status.DEFECT || bike.getStatus() == Status.UNDER_REPAIR){
            throw new BikeNotAvailableException(bikeName);
        }

        if(!customerRepository.findByLastName(cusLastName).isPresent()){
            customerRepository.save(new Customer(cusLastName));
        }
        Customer cus = customerRepository.findByLastName(cusLastName).get();
        cus.addBike(bike);
        customerRepository.save(cus);
    }

    /**
     * Methode zur Zückgabe eines Fahrrads
     *
     * @param cusLastName Name des Kunden
     * @param bikeName Name des Fahrrads
     * @throws CustomerNotFoundException Exception
     */
    @Transactional
    public void returnBike(String cusLastName, String bikeName) throws CustomerNotFoundException, BikeNotAvailableException {
        Customer customer = customerRepository.findByLastName(cusLastName)
                .orElseThrow(() -> new CustomerNotFoundException(cusLastName));

        Bike bike = bikeRepository.findByName(bikeName)
                .orElseThrow(() -> new BikeNotAvailableException(bikeName));

        if (customer.getBikes().contains(bike)) {
            customer.getBikes().remove(bike);
        }else{
            throw new IllegalArgumentException("This Bike is not lent by Mr./Mrs. " + cusLastName);
        }
        customerRepository.save(customer);
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
    public void removeBike(String bikeName) throws BikeNotAvailableException {
        Bike bike = bikeRepository.findByName(bikeName)
                .orElseThrow(() ->  new BikeNotAvailableException(bikeName));
        bikeRepository.delete(bike);
        //save?
    }

    /**
     * Methode zum Makieren eines repariertem Fahhrad
     * es wird auf dem Status Status.AVAILABLE gesetzt
     * @param bikeName Name des Fahrrads
     */
    public void markStatusRepaired(String bikeName) throws BikeNotAvailableException {
       Bike bike = bikeRepository.findByName(bikeName)
                .orElseThrow(() ->  new BikeNotAvailableException(bikeName));

//        bike.setStatus(Status.REPAIRED);
        bike.setStatus(Status.AVAILABLE);
        bikeRepository.save(bike);
    }

    /**
     * Methode zum melden eines defekten Fahrrads
     * @param bikeName Name des Fahrrads
     */
    public void reportDefect(String bikeName) throws BikeNotAvailableException {
        Bike bike = bikeRepository.findByName(bikeName)
                .orElseThrow(() -> new BikeNotAvailableException(bikeName));

        bike.setStatus(Status.DEFECT);

        bikeRepository.save(bike);
    }

}
