package com.oo2.grupo15.controllers;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.oo2.grupo15.dtos.LocalidadDTO;
import com.oo2.grupo15.dtos.LugarDTO;
import com.oo2.grupo15.helpers.ViewRouteHelper;
import com.oo2.grupo15.services.ILocalidadService;
import com.oo2.grupo15.services.ILugarService;
import com.oo2.grupo15.services.IProvinciaService;

@Controller
@RequestMapping("/lugares")
public class LocalidadController {

	private ILocalidadService localidadService;
	private IProvinciaService provinciaService;
	private ILugarService lugarService;
	

	public LocalidadController(ILocalidadService localidadService, IProvinciaService provinciaService,
			ILugarService lugarService) {
		this.localidadService = localidadService;
		this.provinciaService = provinciaService;
		this.lugarService = lugarService;
	}

	@GetMapping("/")
    public ModelAndView index(){
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.LUGAR_INDEX);
		mAV.addObject("provincias", provinciaService.getAll()); 
        mAV.addObject("localidades", List.of());
        mAV.addObject("lugares", List.of());
        return mAV;
    }
	
    @GetMapping("/porProvincia")
    @ResponseBody 
    public List<LocalidadDTO> getLocalidadesPorProvincia(@RequestParam("provinciaId") int provinciaId) {
        if (provinciaId == 0) { 
            return List.of(); 
        }
        return localidadService.getLocalidadesByProvincia(provinciaId);
    }
    
    @GetMapping("/porLocalidad")
    @ResponseBody 
    public List<LugarDTO> getLugaresByLocalidad(@RequestParam("localidadId") int localidadId) {
        if (localidadId == 0) { 
            return List.of(); 
        }
        return lugarService.getLugaresByLocalidad(localidadId);
    }

}
