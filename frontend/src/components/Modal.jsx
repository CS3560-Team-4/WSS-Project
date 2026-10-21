import { useEffect, useId, useRef, useState } from 'react';
import './css/Modal.css';

const focusableSelector = [
  'a[href]',
  'button:not([disabled])',
  'input:not([disabled]):not([type="hidden"])',
  'select:not([disabled])',
  'textarea:not([disabled])',
  '[tabindex]:not([tabindex="-1"])',
].join(',');

const getTopmostDialog = () => {
  const dialogs = document.querySelectorAll('.modal-dialog:not(.modal-dialog--closing)');
  return dialogs[dialogs.length - 1] || null;
};

const getFocusableElements = (dialog) => Array.from(dialog.querySelectorAll(focusableSelector))
  .filter((element) => {
    let currentElement = element;

    while (currentElement) {
      const style = window.getComputedStyle(currentElement);
      if (
        currentElement.hidden
        || currentElement.getAttribute('aria-hidden') === 'true'
        || style.display === 'none'
        || style.visibility === 'hidden'
      ) return false;
      if (currentElement === dialog) break;
      currentElement = currentElement.parentElement;
    }

    return true;
  });

const focusDialog = (dialog) => {
  const focusableElements = getFocusableElements(dialog);
  const focusTarget = focusableElements.find((element) => element.hasAttribute('data-initial-focus'))
    || focusableElements.find((element) => !element.matches('.modal-close'))
    || focusableElements[0]
    || dialog;
  focusTarget.focus({ preventScroll: true });
};

const MODAL_EXIT_MS = 180;

const Modal = ({ isOpen, onClose, closeButton, title, titleClassName = '', width = 'max-w-md', children }) => {
  const dialogRef = useRef(null);
  const onCloseRef = useRef(onClose);
  const dialogId = useId();
  const [present, setPresent] = useState(isOpen);
  const lastContent = useRef({ children, title, titleClassName });

  if (isOpen) lastContent.current = { children, title, titleClassName };

  useEffect(() => {
    if (isOpen) {
      setPresent(true);
      return undefined;
    }

    const reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    const timer = setTimeout(() => setPresent(false), reduceMotion ? 0 : MODAL_EXIT_MS);
    return () => clearTimeout(timer);
  }, [isOpen]);

  useEffect(() => {
    onCloseRef.current = onClose;
  }, [onClose]);

  useEffect(() => {
    if (!isOpen) return undefined;

    const dialog = dialogRef.current;
    if (!dialog) return undefined;

    const previousFocus = document.activeElement;
    const heading = dialog.querySelector('.dialog-title, [data-dialog-title], h1, h2');

    if (heading) {
      const nextHeadingId = `${dialogId}-title`;
      heading.id = nextHeadingId;
      dialog.setAttribute('aria-labelledby', nextHeadingId);
    } else {
      dialog.removeAttribute('aria-labelledby');
    }

    if (getTopmostDialog() === dialog) focusDialog(dialog);

    const handleKeyDown = (event) => {
      if (getTopmostDialog() !== dialog) return;

      if (event.key === 'Escape' && closeButton) {
        event.preventDefault();
        onCloseRef.current?.();
        return;
      }

      if (event.key !== 'Tab') return;

      const focusableElements = getFocusableElements(dialog);
      if (focusableElements.length === 0) {
        event.preventDefault();
        dialog.focus();
        return;
      }

      const first = focusableElements[0];
      const last = focusableElements[focusableElements.length - 1];

      if (!dialog.contains(document.activeElement)) {
        event.preventDefault();
        (event.shiftKey ? last : first).focus();
      } else if (event.shiftKey && document.activeElement === first) {
        event.preventDefault();
        last.focus();
      } else if (!event.shiftKey && document.activeElement === last) {
        event.preventDefault();
        first.focus();
      }
    };

    const keepFocusInside = (event) => {
      if (getTopmostDialog() !== dialog) return;

      if (!dialog.contains(event.target)) {
        focusDialog(dialog);
      }
    };

    document.addEventListener('keydown', handleKeyDown);
    document.addEventListener('focusin', keepFocusInside);

    return () => {
      document.removeEventListener('keydown', handleKeyDown);
      document.removeEventListener('focusin', keepFocusInside);
      const remainingTopmostDialog = getTopmostDialog();
      if (remainingTopmostDialog) {
        if (previousFocus instanceof HTMLElement && remainingTopmostDialog.contains(previousFocus)) {
          previousFocus.focus({ preventScroll: true });
        } else {
          focusDialog(remainingTopmostDialog);
        }
      } else if (previousFocus instanceof HTMLElement && previousFocus.isConnected) {
        previousFocus.focus({ preventScroll: true });
      }
    };
  }, [closeButton, dialogId, isOpen]);

  if (!isOpen && !present) return null;

  const closing = !isOpen;
  const content = lastContent.current;

  const dialogWidth = width === 'max-w-6xl' ? 'modal-dialog--wide' : '';

  return (
    <div className={`modal-backdrop ${closing ? 'modal-backdrop--closing' : ''}`}>
      <div
        className={`modal-dialog ${dialogWidth} ${closeButton ? 'modal-dialog--dismissible' : ''} ${closing ? 'modal-dialog--closing' : ''}`}
        role="dialog"
        aria-modal="true"
        aria-label="Game dialog"
        tabIndex={-1}
        ref={dialogRef}
      >
        {closeButton && (
          <div className="modal-header">
            {content.title && <h2 className={`dialog-title ${content.titleClassName}`}>{content.title}</h2>}
            <button
              type="button"
              onClick={onClose}
              className="modal-close"
              aria-label="Close dialog"
            >
              <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" aria-hidden="true"><path d="m6 6 12 12M18 6 6 18" /></svg>
            </button>
          </div>
        )}

        <div className="modal-content">{content.children}</div>
      </div>
    </div>
  );
};

export default Modal;
