document.addEventListener("click", (event) => {
  if (!(event.target instanceof Element)) {
    return;
  }

  const openButton = event.target.closest("[data-modal-open]");
  if (openButton) {
    const modal = document.getElementById(openButton.dataset.modalOpen);
    if (modal) {
      modal.showModal();
    }
    return;
  }

  const closeButton = event.target.closest("[data-modal-close]");
  if (closeButton) {
    const modal = closeButton.closest("dialog");
    if (modal) {
      modal.close();
    }
  }
});

document.addEventListener("mousedown", (event) => {
  const modal = event.target;
  if (modal instanceof HTMLDialogElement && modal.open) {
    const bounds = modal.getBoundingClientRect();
    const clickedBackdrop =
      event.clientX < bounds.left ||
      event.clientX > bounds.right ||
      event.clientY < bounds.top ||
      event.clientY > bounds.bottom;

    if (clickedBackdrop) {
      modal.close();
    }
  }
});
