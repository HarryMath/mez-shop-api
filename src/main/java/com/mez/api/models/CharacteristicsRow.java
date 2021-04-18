package com.mez.api.models;

import lombok.Data;

@Data
public class CharacteristicsRow {
    private int id;
    private int engineId;
    private float power;                    // P (кВт)
    private int frequency;                  // Номинальная частота вращения (об/мин)
    private int efficiency;                 // КПД (%)
    private float cosFi;                    // cos fi
    private float electricityNominal220;    // ток номинальный (А) для напряжения 220В
    private float electricityNominal380;    // ток номинальный (А) для напряжения 380В
    private float electricityRatio;         // Iп/Iн
    private float momentsRatio;             // Mп/Мн
    private float momentsMaxRatio;          // Mmax/Мн
    private float momentsMinRatio;          // Mmin/Мн
}
