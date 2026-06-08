import './style.css'
import { appService } from './services.js'

const contentArea = document.getElementById('content-area');

// Navigation Logic
document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', (e) => {
        document.querySelectorAll('.nav-item').forEach(nav => nav.classList.remove('active'));
        e.target.classList.add('active');
        const view = e.target.getAttribute('data-view');
        renderView(view);
    });
});

async function renderView(view) {
    contentArea.innerHTML = '<p>Carregando...</p>';
    
    try {
        switch(view) {
            case 'view-vacinas':
                contentArea.innerHTML = `
                    <h2>Consultar Vacinas Recomendadas</h2>
                    <div class="form-group">
                        <label>Informe sua idade:</label>
                        <input type="number" id="idade-input" class="form-control" placeholder="Ex: 25">
                    </div>
                    <button class="btn" id="btn-consultar">Consultar</button>
                    <div id="resultados" style="margin-top: 2rem;"></div>
                `;
                document.getElementById('btn-consultar').addEventListener('click', async () => {
                    const idade = parseInt(document.getElementById('idade-input').value);
                    if (isNaN(idade)) return;
                    const recs = await appService.consultarPorIdade(idade);
                    const resDiv = document.getElementById('resultados');
                    if (!recs || recs.length === 0) {
                        resDiv.innerHTML = '<p>Nenhuma vacina recomendada para essa idade no momento.</p>';
                    } else {
                        resDiv.innerHTML = recs.map(v => `
                            <div class="card">
                                <h3>${v.nome}</h3>
                                <p>Previne: ${v.doencaPrevenida}</p>
                                <p>Doses necessárias: ${v.dosesNecessarias}</p>
                            </div>
                        `).join('');
                    }
                });
                break;

            case 'view-postos':
                contentArea.innerHTML = `
                    <h2>Localizar Posto de Saúde</h2>
                    <div class="form-group">
                        <label>Região (Opcional):</label>
                        <input type="text" id="regiao-input" class="form-control" placeholder="Ex: Zona Sul">
                    </div>
                    <div class="form-group">
                        <label>Vacina desejada (Opcional):</label>
                        <input type="text" id="vacina-input" class="form-control" placeholder="Ex: Gripe">
                    </div>
                    <button class="btn" id="btn-buscar">Buscar</button>
                    <div id="resultados-postos" style="margin-top: 2rem;"></div>
                `;
                document.getElementById('btn-buscar').addEventListener('click', async () => {
                    const regiao = document.getElementById('regiao-input').value;
                    const vacina = document.getElementById('vacina-input').value;
                    const postos = await appService.buscarPorRegiaoEVacina(regiao, vacina);
                    const resDiv = document.getElementById('resultados-postos');
                    
                    if (!postos || postos.length === 0) {
                        resDiv.innerHTML = '<p>Nenhum posto encontrado com esses critérios.</p>';
                    } else {
                        resDiv.innerHTML = postos.map(p => `
                            <div class="card">
                                <h3>${p.nome}</h3>
                                <p>Endereço: ${p.endereco}</p>
                                <p>Horário: ${p.horarioFuncionamento || p.horario}</p>
                                <p>Telefone: ${p.telefone}</p>
                            </div>
                        `).join('');
                    }
                });
                break;

            case 'view-registrar':
                const [usuarios, vacinas, postos] = await Promise.all([
                    appService.listarUsuarios(),
                    appService.listarVacinas(),
                    appService.listarPostos()
                ]);

                const usuariosOptions = usuarios.map(u => `<option value="${u.id}">${u.nome} (${u.idade} anos)</option>`).join('');
                const vacinasOptions = vacinas.map(v => `<option value="${v.id}">${v.nome}</option>`).join('');
                const postosOptions = postos.map(p => `<option value="${p.id}">${p.nome}</option>`).join('');
                
                contentArea.innerHTML = `
                    <h2>Registrar Vacinação</h2>
                    <div class="form-group">
                        <label>Usuário:</label>
                        <select id="reg-usuario" class="form-control">${usuariosOptions}</select>
                    </div>
                    <div class="form-group">
                        <label>Vacina:</label>
                        <select id="reg-vacina" class="form-control">${vacinasOptions}</select>
                    </div>
                    <div class="form-group">
                        <label>Posto de Saúde:</label>
                        <select id="reg-posto" class="form-control">${postosOptions}</select>
                    </div>
                    <div class="form-group">
                        <label>Data da Aplicação:</label>
                        <input type="date" id="reg-data" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Dose (1, 2, Única, Reforço):</label>
                        <input type="text" id="reg-dose" class="form-control" value="1">
                    </div>
                    <button class="btn" id="btn-registrar">Salvar Registro</button>
                    <div id="reg-feedback" style="margin-top: 1rem; color: #10b981; font-weight: 500;"></div>
                `;
                
                document.getElementById('btn-registrar').addEventListener('click', async () => {
                    try {
                        const uId = parseInt(document.getElementById('reg-usuario').value);
                        const vId = parseInt(document.getElementById('reg-vacina').value);
                        const pId = parseInt(document.getElementById('reg-posto').value);
                        const data = new Date(document.getElementById('reg-data').value);
                        const dose = document.getElementById('reg-dose').value;
                        
                        const usuario = usuarios.find(u => u.id === uId);
                        const vacina = vacinas.find(v => v.id === vId);
                        const posto = postos.find(p => p.id === pId);
                        
                        await appService.registrarVacinacao(usuario, vacina, dose, data, posto);
                        document.getElementById('reg-feedback').innerText = "Vacinação registrada com sucesso!";
                        setTimeout(() => document.getElementById('reg-feedback').innerText = '', 3000);
                    } catch(e) {
                        document.getElementById('reg-feedback').style.color = '#ef4444';
                        document.getElementById('reg-feedback').innerText = "Erro ao registrar. Verifique os campos.";
                    }
                });
                break;

            case 'view-pendencias':
                const usersList = await appService.listarUsuarios();
                const users = usersList.map(u => `<option value="${u.id}">${u.nome}</option>`).join('');
                contentArea.innerHTML = `
                    <h2>Verificar Pendências</h2>
                    <div class="form-group">
                        <label>Selecione o Usuário:</label>
                        <select id="pend-usuario" class="form-control">${users}</select>
                    </div>
                    <button class="btn" id="btn-verificar">Verificar</button>
                    <div id="resultados-pendencias" style="margin-top: 2rem;"></div>
                `;
                
                document.getElementById('btn-verificar').addEventListener('click', async () => {
                    const uId = parseInt(document.getElementById('pend-usuario').value);
                    const usuario = usersList.find(u => u.id === uId);
                    const pendentes = await appService.verificarPendencias(usuario);
                    
                    const resDiv = document.getElementById('resultados-pendencias');
                    if (!pendentes || pendentes.length === 0) {
                        resDiv.innerHTML = '<p style="color:#10b981">Carteira de vacinação em dia! Nenhuma pendência.</p>';
                    } else {
                        resDiv.innerHTML = pendentes.map(v => `
                            <div class="card">
                                <h3 style="color:#ef4444">${v.nome}</h3>
                                <p>Faltam doses. Requeridas: ${v.dosesNecessarias}</p>
                            </div>
                        `).join('');
                    }
                });
                break;

            case 'view-campanhas':
                const campanhas = await appService.listarCampanhas();
                contentArea.innerHTML = `<h2>Campanhas Atuais</h2>`;
                if (!campanhas || campanhas.length === 0) {
                    contentArea.innerHTML += '<p>Nenhuma campanha ativa.</p>';
                } else {
                    contentArea.innerHTML += campanhas.map(c => `
                        <div class="card">
                            <h3>${c.nome}</h3>
                            <p>${c.descricao}</p>
                            <p><strong>Público:</strong> ${c.publicoAlvo}</p>
                            <p><strong>Período:</strong> ${new Date(c.dataInicio).toLocaleDateString()} a ${new Date(c.dataFim).toLocaleDateString()}</p>
                        </div>
                    `).join('');
                }
                break;

            case 'view-usuarios':
                const renderUsers = async () => {
                    const currentUsers = await appService.listarUsuarios();
                    return currentUsers.map(u => `
                        <div class="card" style="display:flex; justify-content:space-between; align-items:center;">
                            <div>
                                <h3>${u.nome}</h3>
                                <p>${u.idade} anos</p>
                            </div>
                            <div style="color:var(--text-muted); font-size:0.8rem">ID: ${u.id}</div>
                        </div>
                    `).join('');
                };

                const initialUsersHtml = await renderUsers();

                contentArea.innerHTML = `
                    <h2>Gerenciar Usuários</h2>
                    <div style="display:flex; gap: 1rem; flex-wrap: wrap;">
                        <div style="flex:1; min-width: 250px;">
                            <div class="card">
                                <h3>Novo Usuário</h3>
                                <div class="form-group">
                                    <label>Nome:</label>
                                    <input type="text" id="novo-nome" class="form-control">
                                </div>
                                <div class="form-group">
                                    <label>Idade:</label>
                                    <input type="number" id="nova-idade" class="form-control">
                                </div>
                                <button class="btn" id="btn-add-user">Cadastrar</button>
                            </div>
                        </div>
                        <div style="flex:1; min-width: 250px;">
                            <h3>Lista de Usuários</h3>
                            <div id="lista-usuarios" style="margin-top:1rem; max-height:400px; overflow-y:auto; padding-right:10px;">
                                ${initialUsersHtml}
                            </div>
                        </div>
                    </div>
                `;
                
                document.getElementById('btn-add-user').addEventListener('click', async () => {
                    const nome = document.getElementById('novo-nome').value;
                    const idade = parseInt(document.getElementById('nova-idade').value) || 0;
                    if (!nome) return;
                    
                    await appService.cadastrarUsuario({ nome, idade });
                    
                    document.getElementById('novo-nome').value = '';
                    document.getElementById('nova-idade').value = '';
                    document.getElementById('lista-usuarios').innerHTML = await renderUsers();
                });
                break;
        }
    } catch(err) {
        contentArea.innerHTML = `<p style="color:red">Erro de comunicação com a API: ${err.message}</p>`;
    }
}

// Initial render
renderView('view-vacinas');
