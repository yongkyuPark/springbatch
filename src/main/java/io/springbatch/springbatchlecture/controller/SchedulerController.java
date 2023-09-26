package io.springbatch.springbatchlecture.controller;

import io.springbatch.springbatchlecture.domain.JobSchedulerRequestVO;
import io.springbatch.springbatchlecture.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scheduler")
public class SchedulerController {

    private final SchedulerService schedulerService;

    @PostMapping("/changeCron")
    public ResponseEntity<String> changeCron(@RequestBody JobSchedulerRequestVO request) throws Exception {
        try{
            schedulerService.changeScheduleExpression(request.getJobName(), request.getNewCronExpression());
            return ResponseEntity.ok("Schedule updated successfully");
        }catch (SchedulerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update schedule");
        }
    }

}
