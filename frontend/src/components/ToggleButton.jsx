import React from 'react';
import './ToggleButton.css';

const ToggleButton = ({ label, isOn, onToggle }) => {

    return (
        <label className="switch-container">
            <span className="switch-label">{label}</span>
            <div className="switch-wrapper">
                <input
                    type="checkbox"
                    checked={isOn}
                    onChange={onToggle}
                    className="switch-input"
                />
                <span className="switch-slider" />
            </div>
        </label>
    );
};

export default ToggleButton;

