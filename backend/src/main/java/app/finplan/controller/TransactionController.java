package app.finplan.controller;

import app.finplan.dto.TransactionCreateDTO;
import app.finplan.dto.TransactionDTO;
import app.finplan.dto.TransactionUpdateDTO;
import app.finplan.exception.ResourceException;
import app.finplan.mapper.TransactionMapper;
import app.finplan.model.Transaction;
import app.finplan.repositories.TransactionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/transactions")
    @ResponseStatus(HttpStatus.OK)
    List<Transaction> list() {
        return transactionRepository.all();
    }

    @PostMapping("/transaction")
    @ResponseStatus(HttpStatus.CREATED)
    TransactionDTO add(@RequestBody TransactionCreateDTO transactionCreateDTO) {
        try {
            return transactionRepository.add(transactionCreateDTO);
        } catch (RuntimeException exception) {
            throw new ResourceException("Cant add transaction " + transactionCreateDTO.toString());
        }
    }

    @PutMapping("/transaction/{id}")
    @ResponseStatus(HttpStatus.OK)
    TransactionDTO update(@PathVariable Long id, @Valid @RequestBody TransactionUpdateDTO transactionUpdateDTO) {
        try {
            return transactionRepository.update(id, transactionUpdateDTO);
        } catch (RuntimeException exception) {
            throw new ResourceException("Cant update transaction " + transactionUpdateDTO.toString());
        }
    }

    @DeleteMapping("/transaction/{id}")
    @ResponseStatus(HttpStatus.OK)
    Long update(@PathVariable Long id) {
        try {
            return transactionRepository.delete(id);
        } catch (RuntimeException exception) {
            throw new ResourceException("Cant delete transaction " + id);
        }
    }

    @DeleteMapping("/transactions")
    @ResponseStatus(HttpStatus.OK)
    void clear() {
        try {
            transactionRepository.clear();
        } catch (RuntimeException exception) {
            throw new ResourceException("Cant clear transactions");
        }
    }
}
