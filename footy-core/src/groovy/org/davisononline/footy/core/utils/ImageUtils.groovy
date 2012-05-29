package org.davisononline.footy.core.utils

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import static org.imgscalr.Scalr.*


/**
 * utilities for image/photo processing in the footy app
 */
class ImageUtils {

    /**
     * @param image the image to convert
     * @param type ie "PNG"
     * @return a byte[] of the encoded image for storing in a blob
     * or acting as the source of an <img/> tag
     */
    static byte[] convertImageToByteArray(image, type) {
        def out = new ByteArrayOutputStream()
        ImageIO.write(image, type, out)
        out.toByteArray()
    }

    static BufferedImage resize(BufferedImage image, int width, int height) {
        /*
        int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType()
        def resizedImage = new BufferedImage(width, height, type)
        def g = resizedImage.createGraphics()
        g.setComposite(AlphaComposite.Src)

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                           RenderingHints.VALUE_INTERPOLATION_BILINEAR)

        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                           RenderingHints.VALUE_RENDER_QUALITY)

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON)

        g.drawImage(image, 0, 0, width, height, null)
        g.dispose()

        resizedImage
        */
        
        resize(image, Method.QUALITY, Mode.FIT_EXACT, height, width, OP_ANTIALIAS)
    }

    static BufferedImage resize(File file, int width, int height) {
        resize(ImageIO.read(file), width, height)
    }

    static BufferedImage read(File file) {
        ImageIO.read(file)
    }
}
