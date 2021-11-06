package com.techshard.future.controller;

import com.techshard.future.dao.entity.Car;
import com.techshard.future.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@RestController
@RequestMapping("/api/car")
public class CarController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private CarService carService;

    @RequestMapping (method = RequestMethod.POST, consumes={MediaType.MULTIPART_FORM_DATA_VALUE},
            produces={MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody ResponseEntity uploadFile(
            @RequestParam (value = "files") MultipartFile[] files) {
        try {
            for(final MultipartFile file: files) {
                carService.saveCars(file.getInputStream());
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch(final Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
    
    @PostMapping ("/checkThreads") 
    		//consumes={MediaType.APPLICATION_JSON_VALUE},
            //produces={MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody CompletableFuture<ResponseEntity> checkThreadsMethod() {
    	ArrayList<Integer> list = new ArrayList<>();
    	for(int i=0; i<=25; i++)
    	{
    		list.add(i);
    	}
    	System.out.println("CarController.checkThreadsMethod()"+list.size());
    	
    	ArrayList<CompletableFuture<ArrayList<Integer>>> responseList = new ArrayList<CompletableFuture<ArrayList<Integer>>>();
    	
    	for(Integer in : list)
    	{
    		responseList.add(carService.checkTreads(in));
    	}
    	
    	CompletableFuture<ArrayList<CompletableFuture<ArrayList<Integer>>>> aa =CompletableFuture.completedFuture(responseList);
        return aa.<ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetCarFailure);
    }

    @RequestMapping (method = RequestMethod.GET, //consumes={MediaType.APPLICATION_JSON_VALUE},
            produces={MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody CompletableFuture<ResponseEntity> getAllCars() {
        return carService.getAllCars().<ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetCarFailure);
    }

    private static Function<Throwable, ResponseEntity<? extends List<Car>>> handleGetCarFailure = throwable -> {
        LOGGER.error("Failed to read records: {}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };
}
