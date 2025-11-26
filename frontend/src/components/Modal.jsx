const Modal = ({ isOpen, onClose, closeButton, width = 'max-w-md', children }) => {
  if (!isOpen) return null;

  return (
    <div className='fixed inset-0 bg-black/70 flex items-center justify-center z-50'>
      <div className={`relative bg-neutral-900 rounded-xl p-5 shadow-xl w-[90%] ${width}`}>
        {closeButton && (
          <button
            onClick={onClose}
            className='absolute top-1 right-3 text-gray-400 hover:text-gray-600 transition text-lg cursor-pointer'
          >
            x
          </button>
        )}

        {children}
      </div>
    </div>
  );
} 

export default Modal;

