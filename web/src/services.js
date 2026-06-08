// src/services.js
// This service now fetches data from the Spring Boot API

const API_BASE_URL = 'http://localhost:8080/api';

export class AppService {

    // Usuario Service
    async cadastrarUsuario(usuario) {
        const response = await fetch(`${API_BASE_URL}/usuarios`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(usuario)
        });
        return await response.json();
    }
    
    async listarUsuarios() {
        const response = await fetch(`${API_BASE_URL}/usuarios`);
        return await response.json();
    }
    
    async registrarVacinacao(usuario, vacina, dose, data, posto) {
        const payload = {
            dose: dose,
            data: data.toISOString().split('T')[0], // format to YYYY-MM-DD
            vacina: { id: vacina.id },
            postoSaude: { id: posto.id },
            usuario: { id: usuario.id }
        };
        const response = await fetch(`${API_BASE_URL}/usuarios/registro`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        return await response.json();
    }
    
    async verificarPendencias(usuario) {
        const response = await fetch(`${API_BASE_URL}/usuarios/${usuario.id}/pendencias`);
        return await response.json();
    }

    // Vacina Service
    async cadastrarVacina(vacina) {
        const response = await fetch(`${API_BASE_URL}/vacinas`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(vacina)
        });
        return await response.json();
    }
    
    async listarVacinas() {
        const response = await fetch(`${API_BASE_URL}/vacinas`);
        return await response.json();
    }
    
    async consultarPorIdade(idade) {
        const response = await fetch(`${API_BASE_URL}/vacinas/idade/${idade}`);
        return await response.json();
    }

    // Posto Service
    async cadastrarPosto(posto) {
        const response = await fetch(`${API_BASE_URL}/postos`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(posto)
        });
        return await response.json();
    }
    
    async listarPostos() {
        const response = await fetch(`${API_BASE_URL}/postos`);
        return await response.json();
    }
    
    async buscarPorRegiaoEVacina(regiao, vacinaNome) {
        const params = new URLSearchParams();
        if (regiao) params.append('regiao', regiao);
        if (vacinaNome) params.append('vacina', vacinaNome);
        
        const response = await fetch(`${API_BASE_URL}/postos/buscar?${params.toString()}`);
        return await response.json();
    }

    // Campanha Service
    async cadastrarCampanha(campanha) {
        const response = await fetch(`${API_BASE_URL}/campanhas`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(campanha)
        });
        return await response.json();
    }
    
    async listarCampanhas() {
        const response = await fetch(`${API_BASE_URL}/campanhas`);
        return await response.json();
    }
}

export const appService = new AppService();
