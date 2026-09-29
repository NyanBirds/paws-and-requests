import React from 'react';
import './Popup.css';

export function Popup({ isOpen, onClose, title, children }) {

    if (!isOpen) return null;

    return (
        <div className="popup-overlay" onClick={onClose}>
            <div className="popup-container" onClick={ (e) => e.stopPropagation() }>
                <div className="popup-header">
                    <h3>{title || "Notification"}</h3>
                    <button className="popup-close-btn" onClick={onClose}>
                        &times;
                    </button>
                </div>
                <div className="popup-content">
                    {children}
                </div>
            </div>
        </div>
    );
}
