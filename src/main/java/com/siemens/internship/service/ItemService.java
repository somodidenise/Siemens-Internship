package com.siemens.internship.service;

import com.siemens.internship.model.Item;
import com.siemens.internship.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    //Constructor based injection
    public ItemService(ItemRepository itemRepository)
    {
        this.itemRepository = itemRepository;
    }
    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    //Changed to thread-safe structures
    private final CopyOnWriteArrayList<Item> processedItems = new CopyOnWriteArrayList<>();
    private final AtomicInteger processedCount = new AtomicInteger(0);

    //Retrieve all items from the database
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    //Methods changed to include error handling
    public Item save(Item item) {
        try {
            return itemRepository.save(item);
        } catch (Exception e) {
            throw new RuntimeException("Item not saved", e);
        }
    }
    public void deleteById(Long id) {
        try {
            itemRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Item not deleted", e);
        }
    }
    public Optional<Item> findById(Long id) {
        try {
            return itemRepository.findById(id);
        } catch (Exception e) {
            System.err.println("Could not find item with Id  " + id + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * The original implementation of the processItemsAsync method returned results immediately
     *  without waiting for asynchronous processing to complete, lacked thread safety for
     *  shared resources and  had minimal error handling.
     *
     *  The new implementation of the method does the following for each item from the database:
     *  Simulates work (sleep)
     *  Updates status to "PROCESSED"
     *  Saves the item back to the database
     *
     *  The method uses shared collections that are thread-safe, processes all items before
     *  returning and logs errors per task
     */

   @Async
   public CompletableFuture<List<Item>> processItemsAsync() {
       //The method retrieves now only the IDs for memory-efficiency
    List<Long> itemIds = itemRepository.findAllIds();
    List<CompletableFuture<Void>> futures = new ArrayList<>();

    for (Long id : itemIds) {
        //A list is used to store futures for later processing
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(100);
                //Null handling added
                Item item = itemRepository.findById(id).orElse(null);
                if(item == null) {
                    return;
                }

                item.setStatus("PROCESSED");
                //Save the item that was processed
                itemRepository.save(item);
                //CopyOnWriteArrayList is thread-safe
                processedItems.add(item);
                //AtomicInteger ensures concurrent updates
                processedCount.incrementAndGet();

            } catch (InterruptedException e) {
                //Error logging and interrupt status restoring
                System.out.println("Error processing item with ID " + id + ": " + e.getMessage());
                Thread.currentThread().interrupt();
            }

        }, executor);
        //Adding to the list to wait for all to complete
        futures.add(future);

    }
    //allOf ensures the completion of all tasks before continuing, and only then returns the processed items
    CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    return allOf.thenApply(v -> processedItems);

        
   }

}

//The original implementation of the processItemsAsync method had several issues: it returned results immediately
// without waiting for asynchronous processing to complete, lacked thread safety for shared resources, had minimal
// error handling, and used a fixed thread pool inefficiently.

