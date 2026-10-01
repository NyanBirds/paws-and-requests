import { useState } from "react";
import styles from "./Gallery.module.css"
export default function Gallery(images = []) {
    const [imgIdx, setImgIdx] = useState(0);
    const count = images.image.length;
    const hasSeveral = count > 1;

    return (
        <div className={styles.container}>
            {hasSeveral && (
                <button className={styles.button} onClick={()=> {
                    if(imgIdx === 0) setImgIdx(count - 1);
                    else setImgIdx(imgIdx - 1)

                }} aria-label="previous image" title="previous image">&#8592;</button>
            )}

            <div className={styles.imageFrame}>
                {images.image.map((img, i) => {
                    return <img
                        className={`${styles.img} ${imgIdx == i ? styles["img-current"] : ""}`}
                        key={i}
                        src={img}
                        alt={`Picture ${i + 1} of ${count}`}
                    />
                })}
            </div>

            {hasSeveral && (
                <button className={styles.button} onClick={()=> {
                    if(imgIdx === count - 1) setImgIdx(0);
                    else setImgIdx(imgIdx + 1)

                    }} aria-label="next image" title="next image" >&#8594;</button>
            )}
        </div>
    )
}
