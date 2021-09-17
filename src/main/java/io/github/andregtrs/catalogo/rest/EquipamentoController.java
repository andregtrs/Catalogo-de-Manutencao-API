package io.github.andregtrs.catalogo.rest;

import io.github.andregtrs.catalogo.model.entity.Equipamento;
import io.github.andregtrs.catalogo.model.repository.EquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/equipamentos")
public class EquipamentoController {

    private final EquipamentoRepository repository;

    @Autowired
    public EquipamentoController(EquipamentoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Equipamento> obterTodos(){
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Equipamento salvar( @RequestBody @Valid Equipamento equipamento){
        return repository.save(equipamento);
    }

    @GetMapping("{id}")
    public Equipamento acharPorId( @PathVariable Integer id ){
        return repository
                .findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado") );
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar( @PathVariable Integer id ){
        repository
                .findById(id)
                .map( equipamento -> {
                    repository.delete(equipamento);
                    return Void.TYPE;
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado") );
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar( @PathVariable Integer id,
                           @RequestBody @Valid Equipamento equipamentoAtualizado ) {
        repository
                .findById(id)
                .map( equipamento -> {
                    equipamento.setNome(equipamentoAtualizado.getNome());
                    equipamento.setCpf(equipamentoAtualizado.getCpf());
                    return repository.save(equipamento);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado") );
    }
}
