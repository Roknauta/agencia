package com.roknauta.agencia.domain.documento;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Emissao extends DocumentoViagem {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "ordem")
    private List<BilheteAereo> bilhetesAereos = new ArrayList<>();
}
