import { useState } from "react";
import styles from "./Gallery.module.css"
export default function Gallery(images = []) {
    const [imgIdx, setImgIdx] = useState(0);


    console.log(images);
    return (
        <div className={styles.container}>
            <button className={`${styles.button} ${styles["button-left"]}`} onClick={()=> {
                if(imgIdx == 0) setImgIdx(images.image.length-1);
                else setImgIdx(imgIdx - 1)

            }} areaLabel="previous image" title="previous image">&#8592;</button>
            {images.image.map((img, i) => {
                return <img className={`${styles.img} ${imgIdx == i ? styles["img-current"] : ""}`} key={i} src={img} alt="bingbangbom"></img>
            })}
            <button className={`${styles.button} ${styles["button-right"]}`} onClick={()=> {
                if(imgIdx == images.image.length - 1) setImgIdx(0);
                else setImgIdx(imgIdx + 1)

            }}areaLabel="next image" title="next image" >&#8594;</button>
        </div>
    )
}