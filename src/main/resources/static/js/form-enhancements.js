const DAY_NAMES = ["SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"];

function onlyDigits(value) {
  return value.replace(/\D/g, "");
}

function maskCep(value) {
  const digits = onlyDigits(value).slice(0, 8);
  if (digits.length <= 5) {
    return digits;
  }
  return `${digits.slice(0, 5)}-${digits.slice(5)}`;
}

function maskPhone(value) {
  const digits = onlyDigits(value).slice(0, 11);
  if (digits.length <= 2) {
    return digits;
  }
  if (digits.length <= 6) {
    return `(${digits.slice(0, 2)}) ${digits.slice(2)}`;
  }
  if (digits.length <= 10) {
    return `(${digits.slice(0, 2)}) ${digits.slice(2, 6)}-${digits.slice(6)}`;
  }
  return `(${digits.slice(0, 2)}) ${digits.slice(2, 7)}-${digits.slice(7)}`;
}

function hydrateMasks() {
  document.querySelectorAll("input[name='cep']").forEach((input) => {
    input.addEventListener("input", () => {
      input.value = maskCep(input.value);
      input.setCustomValidity("");
    });
    input.addEventListener("blur", async () => {
      const cep = onlyDigits(input.value);
      if (cep.length !== 8) {
        return;
      }

      try {
        const response = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
        const data = await response.json();
        if (data.erro) {
          input.setCustomValidity("CEP nao encontrado.");
          input.reportValidity();
          return;
        }
        input.setCustomValidity("");
        const form = input.closest("form");
        const endereco = form?.querySelector("input[name='endereco']");
        if (endereco) {
          const partes = [data.logradouro, data.bairro, data.localidade, data.uf].filter(Boolean);
          endereco.value = partes.join(" - ");
        }
      } catch (error) {
        input.setCustomValidity("");
      }
    });
  });

  document.querySelectorAll("input[name='telefone']").forEach((input) => {
    input.addEventListener("input", () => {
      input.value = maskPhone(input.value);
    });
  });

  const today = new Date().toISOString().slice(0, 10);
  document.querySelectorAll("input[type='date'][name='data']").forEach((input) => {
    if (input.id === "agendamentoData") {
      input.min = today;
    }
  });
}

function optionHasVaccine(option, vacinaId) {
  if (!vacinaId || !option.value) {
    return true;
  }
  return option.dataset.vacinas?.split(",").includes(vacinaId);
}

function updatePostOptions(vacinaSelect, postoSelect) {
  const vacinaId = vacinaSelect.value;
  let selectedStillValid = false;

  Array.from(postoSelect.options).forEach((option) => {
    const allowed = optionHasVaccine(option, vacinaId);
    option.hidden = !allowed;
    option.disabled = !allowed;
    if (option.selected && allowed) {
      selectedStillValid = true;
    }
  });

  if (!selectedStillValid) {
    const firstAllowed = Array.from(postoSelect.options).find((option) => !option.disabled);
    postoSelect.value = firstAllowed?.value ?? "";
  }
}

function setupPostFiltering(vacinaSelector, postoSelector) {
  const vacinaSelect = document.querySelector(vacinaSelector);
  const postoSelect = document.querySelector(postoSelector);
  if (!vacinaSelect || !postoSelect) {
    return;
  }

  const refresh = () => updatePostOptions(vacinaSelect, postoSelect);
  vacinaSelect.addEventListener("change", refresh);
  refresh();
}

function validateScheduleForm(form) {
  const postoSelect = form.querySelector("#agendamentoPosto");
  const dataInput = form.querySelector("#agendamentoData");
  const horarioInput = form.querySelector("#agendamentoHorario");
  if (!postoSelect || !dataInput || !horarioInput) {
    return true;
  }

  horarioInput.setCustomValidity("");
  dataInput.setCustomValidity("");
  const option = postoSelect.selectedOptions[0];
  if (!option || !option.value || !dataInput.value || !horarioInput.value) {
    return true;
  }

  const dayName = DAY_NAMES[new Date(`${dataInput.value}T00:00:00`).getDay()];
  const dias = option.dataset.dias?.split(",") ?? [];
  if (dias.length > 0 && !dias.includes(dayName)) {
    dataInput.setCustomValidity("O posto selecionado nao funciona nesse dia.");
    return false;
  }

  const abertura = option.dataset.horaAbertura;
  const fechamento = option.dataset.horaFechamento;
  if (abertura && fechamento && (horarioInput.value < abertura.slice(0, 5) || horarioInput.value > fechamento.slice(0, 5))) {
    horarioInput.setCustomValidity("Horario fora do funcionamento do posto.");
    return false;
  }

  return true;
}

function hydrateScheduleValidation() {
  setupPostFiltering("#agendamentoVacina", "#agendamentoPosto");
  setupPostFiltering("#vacinaId", "#postoId");

  const form = document.querySelector("form[action$='/agendamentos']");
  if (!form) {
    return;
  }

  form.querySelectorAll("select,input").forEach((field) => {
    field.addEventListener("change", () => validateScheduleForm(form));
    field.addEventListener("input", () => validateScheduleForm(form));
  });

  form.addEventListener("submit", (event) => {
    if (!validateScheduleForm(form)) {
      event.preventDefault();
      form.reportValidity();
    }
  });
}

document.addEventListener("DOMContentLoaded", () => {
  hydrateMasks();
  hydrateScheduleValidation();
});
