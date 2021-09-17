package io.github.andregtrs.catalogo.rest;

import io.github.andregtrs.catalogo.model.entity.Equipamento;
import io.github.andregtrs.catalogo.model.entity.ServicoPrestado;
import io.github.andregtrs.catalogo.model.repository.EquipamentoRepository;
import io.github.andregtrs.catalogo.model.repository.ServicoPrestadoRepository;
import io.github.andregtrs.catalogo.rest.dto.ServicoPrestadoDTO;
import io.github.andregtrs.catalogo.util.BigDecimalConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/servicos-prestados")
@RequiredArgsConstructor
public class ServicoPrestadoController  {

    private final EquipamentoRepository equipamentoRepository;
    private final ServicoPrestadoRepository repository;
    private final BigDecimalConverter bigDecimalConverter;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServicoPrestado salvar( @RequestBody @Valid ServicoPrestadoDTO dto ){
        LocalDate data = LocalDate.parse(dto.getData(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Integer idEquipamento = dto.getIdEquipamento();

        Equipamento equipamento =
                equipamentoRepository
                        .findById(idEquipamento)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST, "Equipamento inexistente."));


        ServicoPrestado servicoPrestado = new ServicoPrestado();
        servicoPrestado.setDescricao(dto.getDescricao());
        servicoPrestado.setData( data );
        servicoPrestado.setEquipamento(equipamento);
        servicoPrestado.setValor( bigDecimalConverter.converter(dto.getPreco())  );

        return repository.save(servicoPrestado);
    }

    @GetMapping
    public List<ServicoPrestado> pesquisar(
            @RequestParam(value = "nome", required = false, defaultValue = "") String nome,
            @RequestParam(value = "mes", required = false) Integer mes
    ) {
        return repository.findByNomeEquipamentoAndMes("%" + nome + "%", mes);
    }
}