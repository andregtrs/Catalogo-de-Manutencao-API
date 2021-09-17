package io.github.andregtrs.catalogo.model.repository;

import io.github.andregtrs.catalogo.model.entity.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Integer> {
}

